# Ideas

* account management
* n-people chat room
	* A direct conversation could be implemented as a 2 person chat room.
* profile
	* friend list
* Send pictures
	* Postponed!
* (maybe) send vides
	* Postponed!
* (maybe) send files
	* Postponed!
* The client would have to implement bidirectional communication between client and server (e.g. WebRTC).
	* This has been postponed!
* Content server
	* When users send messages to one another they might include attachments such as pictures, videos, audio files or any file in general.  To minimize the size of the message, i.e. so that the message does not include the file itself, the message would contain information that points to this content, e.g. the SHA512 of the file, and the file itself would be stored at the content services, accessible via the SHA512 of tile file.
		* Potentially include access control.  I.e. you can only request the file if you're logged in and have the correct permissions.
