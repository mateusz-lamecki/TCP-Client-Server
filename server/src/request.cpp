#include "request.h"

#include "utils.h"


namespace sk2 {

namespace request {


Response LoginAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);
    std::string user_token = res.get_user_token(words[1], words[2]);
    if(user_token != res.NON_EXISTING_TOKEN) {
        return Response(std::make_unique<request::OkStatus>(), user_token);
    } else {
        return Response(std::make_unique<request::InvalidPasswordStatus>());
    }

}

Response RegisterAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);

    bool success = res.register_user(words[1], words[2]);
    // Check wherher provided login is not already used
    if(success) {
        return Response(std::make_unique<request::OkStatus>());
    } else {
        return Response(std::make_unique<request::LoginTakenStatus>());
    }
}

Response PublishAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);
    
    auto user = res.get_user(words[1]);
    // Check whether provided user exists
    if(user.has_value()) {
        res.publish_message(words[2], words[3]);
        return Response(std::make_unique<request::OkStatus>());
    } else {
        return Response(std::make_unique<request::InvalidTokenStatus>());
    }
}

Response SubscribeAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);
    
    auto user = res.get_user(words[1]);
    // Check whether provided user exists
    if(!user.has_value()) {
        return Response(std::make_unique<request::InvalidTokenStatus>());
    }

    // Check whether provided topic exists
    if(!res.is_topic(words[2])) {
        return Response(std::make_unique<request::InvalidTopicStatus>());
    }

    res.subscribe_topic(words[1], words[2]);
    return Response(std::make_unique<request::OkStatus>());
}

Response UnsubscribeAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);
    
    auto user = res.get_user(words[1]);
    // Check whether provided user exists
    if(!user.has_value()) {
        return Response(std::make_unique<request::InvalidTokenStatus>());
    }

    // Check whether provided topic exists
    if(!res.is_topic(words[2])) {
        return Response(std::make_unique<request::InvalidTopicStatus>());
    }

    res.unsubscribe_topic(words[1], words[2]);
    return Response(std::make_unique<request::OkStatus>());
}

Response ReadMessagesAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);

    auto topic = res.get_topic(words[1]);
    if(!topic.has_value()) {
        return Response(std::make_unique<request::InvalidTopicStatus>());
    }

    std::string messages_in_topic = topic.value().messages_to_str(request::DELIMITER2);
    return Response(std::make_unique<request::OkStatus>(), messages_in_topic);
}

Response ReadTopicsAction::handle(std::string request_raw, resources::Resources& res) {
    auto words = utils::split_string(request_raw, request::DELIMITER);

    auto user = res.get_user(words[1]);
    // Check whether provided user exists
    if(!user.has_value()) {
        return Response(std::make_unique<request::InvalidTokenStatus>());
    }

    std::string subscribed_topics = user.value().topics_subscribed_to_str(request::DELIMITER);
    return Response(std::make_unique<request::OkStatus>(), subscribed_topics);
}

Response InvalidAction::handle(std::string request_raw, resources::Resources& res) {
    // TODO;
}


std::string LoginAction::to_string() { return "LOGIN"; }
std::string RegisterAction::to_string() { return "REGISTER"; }
std::string PublishAction::to_string() { return "PUBLISH"; }
std::string SubscribeAction::to_string() { return "SUBSCRIBE"; }
std::string UnsubscribeAction::to_string() { return "UNSUBSCRIBE"; }
std::string ReadMessagesAction::to_string() { return "READ_MESSAGES"; }
std::string ReadTopicsAction::to_string() { return "READ_TOPICS"; }
std::string InvalidAction::to_string() { return "INVALID"; }

int LoginAction::get_n_params() { return 2; }
int RegisterAction::get_n_params() { return 2; }
int PublishAction::get_n_params() { return 3; }
int SubscribeAction::get_n_params() { return 2; }
int UnsubscribeAction::get_n_params() { return 2; }
int ReadMessagesAction::get_n_params() { return 1; }
int ReadTopicsAction::get_n_params() { return 1; }
int InvalidAction::get_n_params() { return 0; }

std::string OkStatus::to_string() { return "OK"; }
std::string InvalidPasswordStatus::to_string() { return "INVALID_PASSWORD"; }
std::string LoginTakenStatus::to_string() { return "LOGIN_TAKEN"; }
std::string InvalidTokenStatus::to_string() { return "INVALID_TOKEN"; }
std::string InvalidTopicStatus::to_string() { return "INVALID_TOPIC"; }
std::string OtherErrorStatus::to_string() { return "OTHER_INVALID_STATUS"; }


std::unique_ptr<Action> Action::detect_action(std::string raw_action) {
    if(raw_action == "LOGIN") return std::unique_ptr<Action>{ new LoginAction() };
    else if(raw_action == "REGISTER")  return std::unique_ptr<Action>{ new RegisterAction() };
    else if(raw_action == "PUBLISH")  return std::unique_ptr<Action>{ new PublishAction() };
    else if(raw_action == "SUBSCRIBE")  return std::unique_ptr<Action>{ new SubscribeAction() };
    else if(raw_action == "UNSUBSCRIBE")  return std::unique_ptr<Action>{ new UnsubscribeAction() };
    else if(raw_action == "READ_MESSAGES")  return std::unique_ptr<Action>{ new ReadMessagesAction() };
    else if(raw_action == "READ_TOPICS")  return std::unique_ptr<Action>{ new ReadTopicsAction() };
    return std::unique_ptr<Action>{ new InvalidAction() };
}

std::unique_ptr<Status> Status::detect_status(std::string raw_status) {
    if(raw_status == "OK") return std::unique_ptr<Status>{ new OkStatus() };
    else if(raw_status == "INVALID_PASSWORD")  return std::unique_ptr<Status>{ new InvalidPasswordStatus() };
    else if(raw_status == "LOGIN_TAKEN")  return std::unique_ptr<Status>{ new LoginTakenStatus() };
    else if(raw_status == "INVALID_TOKEN")  return std::unique_ptr<Status>{ new InvalidTokenStatus() };
    else if(raw_status == "INVALID_TOPIC")  return std::unique_ptr<Status>{ new InvalidTopicStatus() };
    else if(raw_status == "OTHER_ERROR")  return std::unique_ptr<Status>{ new OtherErrorStatus() };
}


Response::Response(std::unique_ptr<Status> status, std::string auxilary_out) {
    this->status_str = status->to_string();
    this->auxilary_out = auxilary_out;
}

std::string Response::to_string() {
    return status_str + DELIMITER + auxilary_out;
}

} // namespace request

} // namespace sk2
