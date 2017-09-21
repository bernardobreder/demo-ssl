#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#ifdef WIN32
#include <winsock2.h>
#include <windows.h>
#else
#include <netdb.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#endif

#define MAX_GET_COMMAND 255
#define BUFFER_SIZE 255

int parse_url(const char *uri, char **host, char **path) {
	char *pos = strstr(uri, "//");
	if (!pos) {
		return -1;
	}
	*host = pos + 2;
	pos = strchr(*host, '/');
	if (!pos) {
		*path = 0;
	} else {
		*pos = '\0';
		*path = pos + 1;
	}
	return 0;
}

int http_get(int connection, const char *path, const char *host) {
	static char get_command[MAX_GET_COMMAND];
	sprintf(get_command, "GET /%s HTTP/1.1\r\n", path);
	if (send(connection, get_command, strlen(get_command), 0) == -1) {
		return -1;
	}
	sprintf(get_command, "Host: %s\r\n", host);
	if (send(connection, get_command, strlen(get_command), 0) == -1) {
		return -1;
	}
	sprintf(get_command, "Connection: close\r\n\r\n");
	if (send(connection, get_command, strlen(get_command), 0) == -1) {
		return -1;
	}
	return 0;
}

void display_result(int connection) {
	size_t received = 0;
	static char recv_buf[BUFFER_SIZE + 1];
	while ((received = recv(connection, recv_buf, BUFFER_SIZE, 0)) > 0) {
		recv_buf[received] = '\0';
		printf("%s", recv_buf);
	}
	printf("\n");
}

int http(const char *host, const char *path) {
	int client_connection;
	struct hostent *host_name;
	struct sockaddr_in host_address;
#ifdef WIN32
	WSADATA wsaData;
#endif
	printf("Connecting to host '%s'\n", host);
#ifdef WIN32
	if (WSAStartup(MAKEWORD(2, 2), &wsaData) != NO_ERROR) {
		fprintf(stderr, "Error, unable to initialize winsock\n");
		return 2;
	}
#endif
	client_connection = socket(PF_INET, SOCK_STREAM, 0);
	if (!client_connection) {
		fprintf(stderr, "Error, unable to create local socket\n");
		return 2;
	}
	host_name = gethostbyname(host);
	if (!host_name) {
		fprintf(stderr, "Error in name resolution\n");
		return 3;
	}
	host_address.sin_family = AF_INET;
	host_address.sin_port = htons(80);
	memcpy(&host_address.sin_addr, host_name->h_addr_list[0], sizeof(struct in_addr));
	if (connect(client_connection, (struct sockaddr*) &host_address, sizeof(host_address)) == -1) {
		fprintf(stderr, "Unable to connect to host\n");
		return 4;
	}
	printf("Retrieving document: '%s'\n", path);
	http_get(client_connection, path, host);
	display_result(client_connection);
	printf("Shutting down\n");
#ifdef WIN32
	closesocket(client_connection);
	WSACleanup();
#else
	close(client_connection);
#endif
	return 0;
}
