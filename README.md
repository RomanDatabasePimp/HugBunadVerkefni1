# Hugbúnaðarverkefni 1

Application: Very Wow Chat

## Setup

Prerequisites

* neo4j: <https://neo4j.com>
* mongodb
* redis

## RESET

MONGO
```js
use very_wow_chat
db.dropDatabase()
```

NEO4j
```
match (n)-[r]-(m) delete n,r,m;
match (n) delete n;
```




## Git

### Initial setup

Configure your user name and email.

```bash
git config user.name "John Doe"
git config user.email johndoe@example.com
```

If you have staged your file, e.g. `git add A B C`, with an incorrect username/email, you can run,  

```bash
git commit --amend --reset-author
```

### Create branch locally and push it upstream (GitHub)

To create a new branch (locally)

```bash
git branch NAME_OF_BRANCH
```

and to switch to that branch

```bash
git checkout NAME_OF_BRANCH
```

To create the branch upstream and push the local changes,

```bash
git push --set-upstream origin dh1
```

### Pull changes off of main branch

On your local machine

```bash
git pull origin master
```

TODO: figure out how to merge local branches

## Info

### Roman's server

* 85.220.46.169:8443
* http://85.220.46.169:8443/hello

Hello world!

## Hlekkir
# Fyrir Skilaverkfni 4 CODE REVIEW
  - clonið branchið
  - git clone -b skilaverk4 https://github.com/RomanDatabasePimp/HugBunadVerkefni1.git
  - viola :)

## 

```
# Delete nodes
match (n)-[r]-(m) delete n,r,m;
match (n) delete n;

# Show nodes
match (n) return n;

use very_wow_chat
db.chatMessage.drop()
```

## Hlekkir

Google Drive mappa: <https://drive.google.com/drive/folders/1LK9SQP_2fKv09Pbfxk-JckEw37gyObZT>

