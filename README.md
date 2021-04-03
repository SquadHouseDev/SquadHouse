
# SquadHouse/KekHouse/PepeHouse ??

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
**SquadHouse** is an audio-chat application designed to allow and facilitate conversations between individuals on the platform concerning any perceivable topic they wish to entertain.

### App Evaluation
- **Category:** Social/Audio
- **Mobile:** Will be a purely mobile experience to allow users freedom of hearing/joining in on audio rooms on the fly.
- **Story:** Allows user to share their opinions and have conversations with people in various fields/interests.
- **Market:** Anyone that wants to talk about current events or hear what professionals are conversing about in their respective fields.
- **Habit:** Users can open audio rooms at any point and invite people they want to converse with as they wish or make the room public for free entry/leave.
- **Scope:** Rather narrow scope of audio rooms, similar even to podcasts/radio sessions but could branch out to some functionality similar to the 'live' features found commonly on platforms like Instagram, TikTok, so on so forth.

### Demo
<img src="/docs/demo/v1.0.0-demo.gif" width=200>


## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] User can sign up (basic basic sign up w/ username password combination email only)
* [ ] User can verify their phone number (required to be able to start audio rooms)
* [ ] User can sign up via phone number.
* [x] User can logout of their account
* [x] Users can search up other users.
* [ ] User can create a new audio room with designated participants/groups of people.
* [ ] User can see currently open audio rooms of people they follow.
* [ ] User can see audio rooms of other topics they might possibly be interested in.
* [ ] User can see other people currently in the audio room they are with
* [x] User can see their own profile 
* [x] User can see other people's profiles (from search bar) 
* [ ] User can edit their own interests

**Optional Nice-to-have Stories**
* [ ] User can connect their account to other social medias: Twitter, Instagram, etc.
* [ ] User can search up topics/interests.
* [ ] User can endorse/verify new users.
* [ ] User can deactivate their account.
* [ ] User can create a new event/schedule it
* [ ] User can create a group
* [ ] User can invite people.

### 2. Screen Archetypes

* Login Screen
   * User can sign up via phone number.
* Home Screen
   * User can create a new audio room with designated participants/groups of people.
   * User can see currently open audio rooms of people they follow.
   * User can see audio rooms of other topics they might possibly be interested in.
* New Event Screen/Page
   * User can create a new event/schedule it
* Search Screen
   * User can search up topics/interests/users.
* Notifications Screen
   * User can see announcements/notifications from clubhouse
* Profile Screen
   * User can see their own profile and the profiles of others
* Interests Screen
   * User can toggle which interests they have
* Account Screen
   * User can logout/deactivate their account

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Feed/Page
* Search User/Interests/Groups
* Invite Tab
* Event Tab
* Activity Tab
* Profile Tab

**Flow Navigation** (Screen to Screen)

* Login Screen
   * Home
   * Waiting Screen ?
* Registration Screen
   * Home
   * Waiting Screen ?
* Home Feed
   * Search Screen
   * Event Screen
   * Notifications Screen
   * Profile Screen
* Profile Screen
   * Account Screen
   * Interests Screen
   * What's New/ FAQ/ Community Guidelines/Terms of Service/Privacy policy

## Wireframes
#### Home page
<img src="/docs/wireframes/HomePage.png" width=200>

#### Event Details
<img src="/docs/wireframes/NewEventDetailsPage.png" width=200>

#### New Event Page
<img src="/docs/wireframes/NewEventPage.png" width=200>

#### Profile Page
<img src="/docs/wireframes/ProfilePage.png" width=200>

#### Settings Page
<img src="/docs/wireframes/SettingsPage.png" width=200>

#### Interests Page
<img src="/docs/wireframes/InterestsSettings.png" width=200>

#### Account Settings Page
<img src="/docs/wireframes/AccountSettingsPage.png" width=200>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema
### Models
#### User
| Property      | Type     | Description |
| ------------- | -------- | ------------|
| objectId      | String   | unique id for the user post (default field) |
| createdAt     | DateTime | date when post is created (default field) |
| updatedAt     | DateTime | date when post is last updated (default field) |
| emailVerified     | boolean | user has verified through email |
| biography       | String   | user biography |
| image       | File   | image uploaded by author |
| username       | String   | username set by user |
| firstname       | String   | first name set by user |
| lastname       | String   | last name set by user |
| password       | String   | password set by user |
| phoneNumber       | String   | user phone number  |
| email       | String   | email address  |
| following       | List<User>   | collection of users that a particular user is following  |
| followers       | List<User>   | collection of users that are following the user  |
| clubs       | List<Club>   | collection of clubs that are followed  |

#### Club
| Property      | Type     | Description |
| ------------- | -------- | ------------|
| objectId      | String   | unique id for the user post (default field) |
| createdAt     | DateTime | date when post is created (default field) |
| updatedAt     | DateTime | date when post is last updated (default field) |
| members     |  List<User> | collection of users defining the club roster  |
| followers     | List<User>  | collection of users following the club  |
| followers     | List<User>  | collection of users following the club  |
| name  |  String | name of the club  |
| description     |  String| description of the club  |
| image |  File| club profile picture  |

#### Room
| Property      | Type     | Description |
| ------------- | -------- | ------------|
| objectId      | String   | unique id for the user post (default field) |
| createdAt     | DateTime | date when post is created (default field) |
| updatedAt     | DateTime | date when post is last updated (default field) |
|title |String  |name of the room |
| clubName|String  | optional field for associated club |
| participants| List<User>  | collection of users defining all users in the room|
| host     | User | User that is the host of the room |
| isActive (*TBD*)     | boolean | status of room being live |
| cohosts     | List<User> | User(s) that have speaker permissions in the room with limited admin privileges|
| startedAt (*TBD*)       | DateTime   | date when room was started  |
| endedAt (*TBD*)       | DateTime   | date when the room ended  |

### Networking
#### List of network requests by screen
   - Home Screen
      - (Read/GET) Query all events taking place later (MAX 1 Day) where user has an interest
      - (Create/POST) Create a new room
   - Login Screen
      - (Read/GET) Query for existing user
    - Signup Screen
      - (Create/POST) Create new user
   - Configure Room Pullup Screen
      - (Create/POST) Create a new room object of type
        - Allow configuration of: Open, Social, Closed
        - Allow configuration of: Topic
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Read/GET) Query user followers
      - (Read/GET) Query user profile image
      - (Read/GET) Query user profile biography
      - (Read/GET) Query user profile firstname
      - (Read/GET) Query user profile lastname
      - (Read/GET) Query user profile username
      - (Update/PUT) Update user profile image
      - (Update/PUT) Update user profile biography
      - (Update/PUT) Update user profile username
      - (Update/PUT) Update user profile firstname
      - (Update/PUT) Update user profile lastname
      - (Update/PUT) Update user profile interests
      - (Update/PUT) Update user profile following
