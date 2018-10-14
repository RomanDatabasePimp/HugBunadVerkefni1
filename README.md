# Hugbúnaðarverkefni 1

Application: Very Wow Chat

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

