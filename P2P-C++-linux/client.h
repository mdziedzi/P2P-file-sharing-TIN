#ifndef CLIENT_H_INCLUDED
#define CLIENT_H_INCLUDED

#include <netinet/in.h>

class Client{
    public:
        Client();
        void send_message();
    private:
        int sock;
        struct sockaddr_in client;
        struct sockaddr_in remote_server;
};


#endif // CLIENT_H_INCLUDED
