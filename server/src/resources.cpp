#include "resources.h"

#include "utils.h"


namespace sk2 {

namespace resources {

/* Topic class */

Topic::Topic(std::string name="") : name(name) { }

void Topic::add_message(std::string message) {
    messages.push_back(message);
}

std::string Topic::messages_to_str(std::string delimiter) {
    return utils::vector_to_str(messages, delimiter);
}

bool Topic::operator ==(const Topic &rhs) const {
    return name == rhs.name;
}

bool Topic::operator <(const Topic &rhs) const {
    return name < rhs.name;
}


/* User class */

User::User(std::string login="", std::string password="") : login(login), password(password) { }

std::string User::get_token() const {
    // TODO: Implement hasing
    return login;
}

std::string User::topics_subscribed_to_str(std::string delimiter) {
    std::vector<std::string> messages(topics_subscribed.size());
    std::copy(topics_subscribed.begin(), topics_subscribed.end(), messages.begin());
    return utils::vector_to_str(messages, delimiter);
}

bool User::pass_matches(std::string password) const {
    return password == this->password;
}

std::set<std::string>& User::get_topics_subscribed() {
    return topics_subscribed;
}

bool User::operator ==(const User &rhs) const {
    return login == rhs.login;
}

bool User::operator <(const User &rhs) const {
    return login < rhs.login;
}


/* Resources class */

Resources::Resources() {
    users["mateusz"] = User("mateusz", "pass");
}

bool Resources::register_user(std::string login, std::string password) {
    auto it = users.find(login);
    if(it != users.end()) return false;

    users[login] = User(login, password);
    return true;
}

std::string Resources::get_user_token(std::string login, std::string password) {
    auto it = users.find(login);
    if(it != users.end() && it->second.pass_matches(password)) {
        return it->second.get_token();
    } else {
        return NON_EXISTING_TOKEN;
    }
}

std::optional<User> Resources::get_user(std::string token) {
    if(users.find(token) != users.end()) return users[token];
    return std::nullopt;
}

void Resources::subscribe_topic(std::string token, std::string topic_id) {
    users[token].get_topics_subscribed().insert(topic_id);
}

void Resources::unsubscribe_topic(std::string token, std::string topic_id) {
    auto it = users[token].get_topics_subscribed().find(topic_id);
    if(it != users[token].get_topics_subscribed().end()) {
        users[token].get_topics_subscribed().erase(it);
    }
}

void Resources::publish_message(std::string topic_id, std::string message) {
    if(topics.find(topic_id) == topics.end()) {
        topics[topic_id] = Topic(topic_id);
    }

    topics[topic_id].add_message(message);
}

std::optional<Topic> Resources::get_topic(std::string topic_id) {
    if(topics.find(topic_id) != topics.end()) return topics[topic_id];
    return std::nullopt;
}

bool Resources::is_topic(std::string topic_id) {
    return topics.find(topic_id) != topics.end();
}

} // namespace resources

} // namespace sk2
