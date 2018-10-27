#include "Node.h"
#include <arpa/inet.h>

Node::Node(sockaddr_in addr)
{
    addr = addr;
}

Node::~Node()
{
    //dtor
}

string Node::get_internet_address(){
    return inet_ntoa(addr.sin_addr);
}
