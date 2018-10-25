#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <stdio.h>
#include <unistd.h>
#include <cstring>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define BUFLEN 512
#define PORT 7000
#define CLIENT_PORT 9002

using namespace std;

int main()
{
    struct sockaddr_in server;
    int sock;

    ssize_t recv_len;
    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    char buf[BUFLEN];
    if (sock == -1) {
        perror("Error while opening stream socket");
        exit(1);
    }
    int broadcast = 1;
    if(setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) < 0){
        perror("Error while setting option to broadcast 1");
        exit(1);
    }
    server.sin_family = AF_INET;
    server.sin_port = htons(PORT);
    server.sin_addr.s_addr = INADDR_ANY;
    if (bind(sock, (struct sockaddr *) &server, sizeof server)== -1) {
        perror("Error while binding stream socket 2");
        exit(1);
    }
    socklen_t length = sizeof(server);
    if (getsockname(sock,(struct sockaddr *) &server, &length) == -1) {
        perror("Error while getting socket name");
        exit(1);
    }
    pid_t pid = fork();
    if (pid == 0)
    {
        // child process
        cout << "Listinig UDP socker port: " << ntohs(server.sin_port) << endl;
        while (true){
            if ((recv_len = recv(sock, buf, BUFLEN, 0)) == -1){
                perror("Error while receiving data");
                exit(1);
            }
            cout << "Greate I have a message!" << endl;
        }
    } else if (pid > 0)
    {
        // parent process
        int sock_client;
        struct sockaddr_in client;
        sock_client = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
        if (sock_client == -1) {
            perror("Error while opening stream socket");
            exit(1);
        }
        int broadcast = 1;
        if(setsockopt(sock_client,SOL_SOCKET,SO_BROADCAST,&broadcast,sizeof(broadcast)) < 0){
            perror("Error while setting option to broadcast");
            exit(1);
        }
        client.sin_family = AF_INET;
        client.sin_port = htons(CLIENT_PORT);
        client.sin_addr.s_addr = INADDR_BROADCAST;
        if (bind(sock_client, (struct sockaddr *) &client, sizeof client)== -1) {
            perror("Error while binding stream socket huj");
            exit(1);
        }
        socklen_t length = sizeof(client);
        if (getsockname(sock,(struct sockaddr *) &client, &length) == -1) {
            perror("Error while getting socket name");
            exit(1);
        }
        //now reply the client with the same data
        char message[BUFLEN];
        ssize_t slen = sizeof(client);
        if (sendto(sock_client, message, strlen(message), 0, (struct sockaddr*) &client, slen) == -1)
        {
            perror("Error while sending");
            exit(1);
        }
    }



    return 0;
}
