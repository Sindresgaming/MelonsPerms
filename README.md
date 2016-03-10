# MelonsPerms
A permissions plugin with Multi-Server and Multi-World support. I got tired of making a permission manager for each project I worked on.

## Configuration
```yml
# MelonPerms Configuration
# Leave Server UUID alone - It will be populated by the plugin upon first startup
server-uuid: undefined
storage:
  
  # How you'd like MelonPerms to store data
  mode: yaml
  yaml:
    # The data file to use
    file: data.yml
  sql:
    # The MySQL server information
    host: localhost
    port: 3306
    database: minecraft
    username: root
    password: ''
    table-prefix: 'mp_'
    # How often to pull group and tracks (in case of changes)
    sync-minutes: 3
# Not currently used
redis:
  enable: false
```

## Commands
The commands for MelonPerms are pretty straight forward.

#### Group Management
[ ] = Required, { } = Optional
* **/p group create [Name]**
Creates a new group with a priority that's 1 higher than any existing

* **/p group delete [Group]**
Deletes a group

* **/p group inherit [Group] [To Inherit]**
Tells [Group] to inheirt permissions from [To Inherit]

* **/p group list**
Lists all groups, organized by priority, ascending

* **/p group prefix [Group] [Prefix|Remove]**
Defines a prefix for a group and automatically adds a space to the end. Say "remove" to remove a defined prefix

* **/p group priority [Group] [Priority]**
Changes the priority for a group. The lowest priority group is your "default" group. Groups with higher priorities will override the permission decisions of lower priority groups.

* **/p group server [Group]**
Toggles the current server being a supported server for the group. If no servers are defined, then all servers will support the group.

* **/p group set [Group] [Permission] {Negated}**
Defines a permission for a group. If {Negated} is true, then the permission will be negated.

* **/p group suffix [Group] [Suffix|Remove]**
Defines a suffix for a group. Say "remove" to remove a defined suffix.

* **/p group unset [Group] [Permission]**
Unsets a defintion for a permission.

* **/p group view [Group]**
Shows information about the provided group

* **/p group world [Group] [World]**
Toggles the provided world being a supported world for the group. If no worlds are defined, then all worlds will support the provided world.

#### User Management
[ ] = Required, { } = Optional
* **/p user addgroup [User] [Group]**
Adds a user to a group

* **/p user bukkitcheck [Player] [Permission]**
Performs a bukkit hasPermission() check on a player, for testing purposes

* **/p user demote [User] {Track}**
Demotes a user on the default (or provided) track

* **/p user prefix [User] [Prefix|Remove]**
Defines a prefix for a user and automatically adds a space at the end. Say "remove" to remove a defined prefix. User prefixes will override group prefixes.

* **/p user promote [User] {Track}**
Promotes a user on the default (or provided) track

* **/p user removegroup [User] [Group]**
Removes a user from the provided group

* **/p user server [User] [Permission]**
Toggles the current server being a supported server for the permission. If no servers are defined, then all servers will support the permission.
 
* **/p user set [User] [Permission] {Negated}**
Defines a permission for a user. If {Negated} is true, then the permission will be negated. User permissions will override group permissions.

* **/p user setgroup [User] [Group]**
Removes any existing group memberships (except the default group) and adds the user to the provided group

* **/p user suffix [User] [Suffix|Remove]**
Defines a suffix for a user. Say "remove" to remove the defined suffix. User suffixes will override group suffixes.

* **/p user super [User]**
Toggles a user being a super user. Super users have full plugin access regardless of OP or permission status. **This command can only be run from console.**

* **/p user unset [User] [Permission]**
Undefined a permission from a user.

* **/p user view [User]**
Shows information about the provided user.

* **/p user world [User] [Permission] [World]**
Toggles the provided world being a supported world for the permission. If no worlds are defined, then all worlds will support the permission.

#### Track Management
[ ] = Required, { } = Optional
* **/p track addgroup [Track] [Group]**
Adds a group to the provided track

* **/p track create [Name]**
Creates a new track

* **/p track default [Track]**
Changes the default track

* **/p track list**
Lists all tracks

* **/p track movegroup [Track] [Group] [Up/Down]**
Moves a group within a track

* **/p track removegroup [Track] [Group]**
Removes a group from the provided track

* **/p track view [Track]**
Shows information for a track
