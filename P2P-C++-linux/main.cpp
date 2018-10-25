#include <iostream>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <unistd.h>
#include <cstring>
#include <arpa/inet.h>
#include "server.cpp"



#define CLIENT_PORT 9002

using namespace std;

int main()
{
    Server server;
    pid_t pid = fork();
    if (pid == 0){
        server.run();
    } else if (pid > 0)
    {
        int sock_client;
        struct sockaddr_in client, remoteServAddr;
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
            perror("Error while binding stream socket.");
            exit(1);
        }

        remoteServAddr.sin_family = AF_INET;
        remoteServAddr.sin_port = htons(SERVER_PORT);
        remoteServAddr.sin_addr.s_addr = INADDR_ANY;

        char message[BUFLEN];

        ssize_t slen = sizeof(remoteServAddr);
        if (sendto(sock_client, message, strlen(message), 0, (struct sockaddr*) &remoteServAddr, slen) == -1)
        {
            perror("Error while sending");
            exit(1);
        }
    }



    return 0;
}
