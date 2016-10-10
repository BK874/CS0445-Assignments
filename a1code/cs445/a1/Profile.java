package cs445.a1;

import java.util.Random;

public class Profile implements ProfileInterface{
    private String name;
    private String about;
    private Set<ProfileInterface> followingSet = new Set<ProfileInterface>();

    /**
     * Initializes the profile's name and "about me" to empty strings.
     */
    
    public Profile(){
	name = "";
	about = "";
    }

    /** 
     * Initializes the profile's name and "about me" to value provided by the caller
     */

    public Profile(String name, String about){
	if(name == null){
	    name = "";
	}
	if(about == null){
	    about = "";
	}
	this.name = name;
	this.about = about;
    }
    
    /**
     * Sets this profile's name.
     */

    public void setName(String newName) throws java.lang.IllegalArgumentException{
	if(newName != null){
	    name = newName;
	}else{
	    throw new IllegalArgumentException("Not a valid name!");
	}
    }

    /**
     * Gets this profile's name
     */

    public String getName(){
	return name;
    }

    /**
     * Sets this profile's "about me" blurb
     */

    public void setAbout(String newAbout) throws java.lang.IllegalArgumentException{
	if(newAbout != null){
	    about = newAbout;
	}else{
	    throw new IllegalArgumentException("Not a valid blurb!");
	}
    }

    /**
     * Gets this profile's "about me" blurb
     */

    public String getAbout(){
	return about;
    }

    /**
     * Adds another profile to this profile's following set
     */
    
    public boolean follow(ProfileInterface other){
	try{
	    return followingSet.add(other);
	}catch(SetFullException e){
	    return false;
	}
    }

    /**
     * Removes the specified profile from this profile's following set.
     */

    public boolean unfollow(ProfileInterface other){
	return followingSet.remove(other);
    }

    /**
     * Returns a preview of this profile's following set.
     */

    public ProfileInterface[] following(int howMany){
	ProfileInterface[] previewArray;
	Object[] followingArray = new Object[followingSet.getCurrentSize()];
	followingArray = followingSet.toArray();
	if(followingArray.length <= howMany){
	    previewArray = new ProfileInterface[followingArray.length];
	    for (int index=0; index < followingArray.length; index++){
		previewArray[index] = (ProfileInterface)followingArray[index];
	    }
	}else{
	    previewArray = new ProfileInterface[howMany];
	    for (int index=0; index < howMany; index++){
		previewArray[index] = (ProfileInterface)followingArray[index];
	    }
	}	
	return previewArray;
    }

    /**
     * Recommend a profile for this profile to follow - a profile followed by one 
     * of this profile's followed profiles. Should not recommend a profile this 
     * profile already follows or this profile itself.
     */
    
    public ProfileInterface recommend(){
	ProfileInterface recommendation = null;
	for(ProfileInterface p : following(2147483647)){
	    ProfileInterface[]followingArray = p.following(2147483647);
	    for(int i=0; i < followingArray.length; i++){
		if(!equals(followingArray[i]) &&
		   !followingSet.contains(followingArray[i])){
		    recommendation = followingArray[i];
		}
	    }
	}
	return recommendation;
    }

}
