# MelonsPerms
A permissions plugin with Multi-Server and Multi-World support. I got tired of making a permission manager for each project I worked on.

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
Toggles the provided world beign a supported world for the group. If no worlds are defined, then all worlds will support the provided world.

