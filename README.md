NetworkMe
=========

NetworkMe is a social media application made by students of Imperial College London.

The idea was put forward by start-up company Ocean Labs and has been developed
as part of a Group project.

To use the application, log in with facebook and click search (GPS enabled).
You can also log into instagram through "Settings" and clicking the button there.
The response is not updated real time at the moment but you will be loffed in.

You can search for any city in the world, the geocoder fails if you search a country.
You can also search key words, use the tabs to navigate between styles of viewing
results. You can also expand the results for more detailed information on the data being displayed.

Investigate the "example" folder for code.


Things to improve
=================
Due to the short time scale of this project, this ws the resulting MVP. Some known issues which can be improved are:
> Fundamental Media Object design. There is duplication of the twitter class code which accounts for both picture
  and non-picture objects. Simple fixes here include creating a new class of say PictureElement which would then have to
  be added as member to both twitter and instagram instances.
  
> Google Map interaction. The use of google maps was not throoughly tested during it implementation, as a result, it may    contain flaws in the code. One obvious flaw is that the map occasionally will crash the application.

> The code has been less than generously documented, for any inquiries, please email terence.tse11@imperial.ac.uk

> There are problems when loading iages. Though we use a third party library (smart image), it does still cause
  jumping in the image view.

> There were also a various amount of API keys that have been used throughout the project, they are most likely static      variables and can easily be identified to change.

> There has been use of a singleton and after some though, this may not be the best way to design the various API      
  Handlers in the project, this may want to be reconsidered.

The scope for this project can be infinitely large and has many challenges, mainly concerning the APIs and their limitations.
