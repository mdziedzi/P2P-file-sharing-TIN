#include "Serializator.h"
#include <vector>

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
        Node node();
        nodes.push_back(*it);
    }
    return nodes;
}
