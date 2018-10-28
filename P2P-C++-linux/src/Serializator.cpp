#include "Serializator.h"
#include <vector>
#include <arpa/inet.h>
#include <iostream>

using namespace std;

Serializator::Serializator()
{
    //ctor
}

Serializator::~Serializator()
{
    //dtor
}

vector <Node> Serializator::get_nodes(vector <struct sockaddr_in> addrs){
    vector <Node> nodes;
    for(vector<struct sockaddr_in>::iterator it = addrs.begin(); it != addrs.end(); ++it) {
        Node node(*it);
        nodes.push_back(node);
    }
    return nodes;
}
