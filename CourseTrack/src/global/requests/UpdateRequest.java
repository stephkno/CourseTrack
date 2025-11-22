package global.requests;

import java.io.Serializable;

public record UpdateRequest (/*ALL DATA I THINK IT WOULD MAKE SENSE TO JUST HAVE UPDATE UPDATE EVERYTHING INSTEAD OF TRYING TO NITPICK RESOURCES ONLY UPDATED PARTS OF THE UI NEEDED
FOR EXAMPLE
Course[] courses, Department[] departments, ...
Basically anything that would need to be updated in the UI right away. Like a course getting added in real time*/) implements Serializable { }
