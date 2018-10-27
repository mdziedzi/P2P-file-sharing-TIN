#ifndef CLIENT_H_INCLUDED
#define CLIENT_H_INCLUDED

#include <netinet/in.h>
#include <vector>

using namespace std;

class Client{
    public:
        Client();
        void send_message();
        vector <struct sockaddr_in> get_active_nodes();
    private:
        int sock;
        struct sockaddr_in client;
        struct sockaddr_in remote_server;
};


#endif // CLIENT_H_INCLUDED
