#pragma once

#include <optional>
#include <memory>
#include <string>
#include <vector>
#include <map>
#include <set>


namespace sk2 {

namespace resources {

class Topic {
    public:
    Topic(std::string name);
    void add_message(std::string message);
    std::string messages_to_str(std::string delimiter);
    bool operator ==(const Topic &rhs) const;
    bool operator <(const Topic &rhs) const;

    private:
    std::string name;
    std::vector<std::string> messages;
};

class User {
    public:
    User(std::string login, std::string password);
    std::string get_token() const;
    std::string topics_subscribed_to_str(std::string delimiter);
    bool pass_matches(std::string password) const;
    std::set<std::string>& get_topics_subscribed();
    bool operator ==(const User &rhs) const;
    bool operator <(const User &rhs) const;

    private:
    std::set<std::string> topics_subscribed;
    std::string login;
    std::string password;
};

class Resources {
    public:
    Resources();
    std::string get_user_token(std::string login, std::string password);
    std::optional<User> get_user(std::string token);
    bool register_user(std::string login, std::string password);
    void subscribe_topic(std::string token, std::string topic_id);
    void unsubscribe_topic(std::string token, std::string topic_id);
    void subscribe(std::string token, std::string topic_id);
    void unsubscribe(std::string token, std::string topic_id);

    void publish_message(std::string topic_id, std::string message);
    std::optional<Topic> get_topic(std::string topic_id);
    bool is_topic(std::string topic_id);

    const std::string NON_EXISTING_TOKEN = "KDKFMF-NON-EXISTING-ASDASD";

    private:
    std::map<std::string, Topic> topics; // TODO: change to unordered_set
    std::map<std::string, User> users; // TODO: change to unordered_set
};

} // namespace resources

} // namespace sk2
