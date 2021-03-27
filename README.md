
# SquadHouse/KekHouse/PepeHouse ??

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
[Description of your app]
'SquadHouse' is an audio-chat application designed to allow and facilitate conversations between individuals on the platform concerning any perceivable topic they wish to entertain.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social/Audio
- **Mobile:** Will be a purely mobile experience to allow users freedom of hearing/joining in on audio rooms on the fly.
- **Story:** Allows user to share their opinions and have conversations with people in various fields/interests.
- **Market:** Anyone that wants to talk about current events or hear what professionals are conversing about in their respective fields.
- **Habit:** Users can open audio rooms at any point and invite people they want to converse with as they wish or make the room public for free entry/leave.
- **Scope:** Rather narrow scope of audio rooms, similar even to podcasts/radio sessions but could branch out to some functionality similar to the 'live' features found commonly on platforms like Instagram, TikTok, so on so forth.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [ ] User can sign up via phone number.
* [ ] User can logout of their account
* [ ] Users can search up other users.
* [ ] User can create a new audio room with designated participants/groups of people.
* [ ] User can see currently open audio rooms of people they follow.
* [ ] User can see audio rooms of other topics they might possibly be interested in.
* [ ] User can see other people currently in the audio room they are with
* [ ] User can see their own profile and the profiles of others and edit with interests
* [ ] User can invite people.

**Optional Nice-to-have Stories**

* [fill in your required user stories here]
* [ ] User can connect their account to other social medias: Twitter, Instagram, etc.
* [ ] User can search up topics/interests.
* [ ] User can endorse/verify new users.
* [ ] User can deactivate their account.
* [ ] User can create a new event/schedule it
* [ ] User can create a group

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
* Home page
<img src="/docs/HomePage.png" width=200>

* Event Details
<img src="/docs/NewEventDetailsPage.png" width=200>
* New Event Page
<img src="/docs/NewEventPage.png" width=200>
* Profile Page
<img src="/docs/ProfilePage.png" width=200>
* Settings Page
<img src="/docs/SettingsPage.png" width=200>
* Interests Page
<img src="/docs/InterestsSettings.png" width=200>
* Account Settings Page
<img src="/docs/AccountSettingsPage.png" width=200>

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
| biography       | String   | user biography |
| username       | String   | username set by user |
| lastname       | String   | last name set by user |
| firstname       | String   | first name set by user |
| password       | String   | password set by user |
| phoneNumber       | String   | user phone number  |
| email       | String   | email address  |
| image       | File   | image uploaded by author |

#### Room
| Property      | Type     | Description |
| ------------- | -------- | ------------|
| objectId      | String   | unique id for the user post (default field) |
| createdAt     | DateTime | date when post is created (default field) |
| updatedAt     | DateTime | date when post is last updated (default field) |
| user     | Pointer to User | address of User object in database |
| host     | User | User that is the host of the room |
| cohosts     | List of User | User(s) that have speaker permissions in the room |
| name       | String   | name of the room |
| startedAt       | DateTime   | date when room was started  |
| endedAt       | DateTime   | date when the room ended  |

### Networking
#### List of network requests by screen
   - Home Screen
      - (Read/GET) Query all events taking place later (MAX 1 Day) where user has an interest
      - (Create/POST) Create a new room
   - Configure Room Pullup Screen
      - (Create/POST) Create a new room object of type
        - Allow configuration of: Open, Social, Closed
        - Allow configuration of: Topic
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
      - (Update/PUT) Update user profile biography
      - (Update/PUT) Update user profile username
      - (Update/PUT) Update user profile firstname
      - (Update/PUT) Update user profile lastname
      - Stretch
        - (Update/PUT) Update user profile interests
