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
#include "client.cpp"


#define CLIENT_PORT 9002

using namespace std;

int main(){
    Server server;
    pid_t pid = fork();
    if (pid == 0){
        server.run();
    } else if (pid > 0){
        Client client;
        client.send_message();
    }
    return 0;
}
