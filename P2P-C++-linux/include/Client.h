#ifndef CLIENT_H
#define CLIENT_H

#include <netinet/in.h>
#include <vector>

using namespace std;

class Client{
    public:
        Client();
        virtual ~Client();
        vector <struct sockaddr_in> get_active_nodes();
    private:
        int sock;
        struct sockaddr_in client;
        struct sockaddr_in broadcast_addr;
        void send_presence_request(void);
        vector <struct sockaddr_in> wait_for_presence_response(void);
};

#endif // CLIENT_H
