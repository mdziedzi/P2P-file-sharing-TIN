#ifndef NODE_H
#define NODE_H
#include <netinet/in.h>
#include <string>

using namespace std;

class Node
{
    public:
        Node(sockaddr_in);
        virtual ~Node();
        string get_internet_address();

    protected:

    private:
        struct sockaddr_in addr;
};

#endif // NODE_H
