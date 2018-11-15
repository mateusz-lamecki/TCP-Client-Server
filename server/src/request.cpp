#include "request.h"
#include "utils.h"

namespace sk2 {

namespace request {

Action detect_action(std::string raw_content) {
    auto words = utils::split_string(raw_content, ' ');
    if(words[0] == "LOGIN") return Action::LOGIN;
    else if(words[0] == "REGISTER") return Action::REGISTER;
    else if(words[0] == "PUBLISH") return Action::PUBLISH;
    else if(words[0] == "SUBSCRIBE") return Action::SUBSCRIBE;
    else if(words[0] == "UNSUBSCRIBE") return Action::UNSUBSCRIBE;
    else if(words[0] == "READ") return Action::READ;
    
    return Action::INVALID;

}

} // namespace request

} // namespace sk2
