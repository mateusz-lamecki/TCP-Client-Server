#include "system_service.h"
#include "request.h"
#include "utils.h"

namespace sk2 {

SystemService::SystemService() { }

request::Response SystemService::handle_request(std::string request_raw, int client_fd) {
    auto words = utils::split_string(request_raw, request::DELIMITER);

    std::unique_ptr<request::Action> action = request::Action::detect_action(words[0]);
    if(dynamic_cast<request::InvalidAction*>(action.get())) {
        return request::Response(std::make_unique<request::OtherErrorStatus>(), "Invalid action provided");
    }

    words.erase(words.begin());
    if((int)words.size() != action->get_n_params()) {
        return request::Response(std::make_unique<request::OtherErrorStatus>(), "Incorrect number of params");
    }

    auto response_of_action = action->handle(request_raw, this->res, client_fd);
    return response_of_action;

}


} // namespace sk2
