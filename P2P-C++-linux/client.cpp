#include "consts.h"
#include <sys/socket.h>

#define BUFLEN 512

class Client{
    public:
        Client();
        void send_message();
    private:
        int sock;
        struct sockaddr_in client;
        struct sockaddr_in remote_server;
};

Client::Client(){
    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == -1) {
        perror("Error while opening stream socket");
        exit(1);
    }
    int broadcast = 1;
    if(setsockopt(sock, SOL_SOCKET, SO_BROADCAST, &broadcast, sizeof(broadcast)) < 0){
        perror("Error while setting option to broadcast");
        exit(1);
    }
    client.sin_family = AF_INET;
    client.sin_port = htons(CLIENT_PORT);
    client.sin_addr.s_addr = INADDR_BROADCAST;
    if (bind(sock, (struct sockaddr *) &client, sizeof client)== -1) {
        perror("Error while binding stream socket.");
        exit(1);
    }
    remote_server.sin_family = AF_INET;
    remote_server.sin_port = htons(SERVER_PORT);
    remote_server.sin_addr.s_addr = INADDR_ANY;

}

void Client::send_message(){
    char message[BUFLEN];
    ssize_t slen = sizeof(remote_server);
    if (sendto(sock, message, strlen(message), 0, (struct sockaddr*) &remote_server, slen) == -1)
    {
        perror("Error while sending");
        exit(1);
    }
}
