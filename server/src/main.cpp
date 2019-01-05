#include <iostream>
#include <cstdlib>

#include "server_service.h"

int main(int argc, char* argv[]) {
    int port = 3552;

    if(argc > 1) {
        port = strtol(argv[1], &argv[1], 10);
    }

    sk2::ServerService service(port);
    service.run();

    return(0);
}
