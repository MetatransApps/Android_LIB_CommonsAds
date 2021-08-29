# License

The license is GPL-2.0-only

# Overview

This is the repository, which abstracts the Ads logic, so the code of our apps use this lib instead of refering directly to Ads implementations, like Google Ads and others.
Of course, you still need to add this 3th party libs in order to use Ads from them, but the code of the app is clean and can be used for building different distributions e.g. with Google Ads, MoPub Ads, Millennial Media Ads, Home Ads, and even without any Ads, without the need to change the main code of the program.
Which Ads to use or not use, could be easily configured in the project, which builds the final APK file.

# Example of configuration
  - https://github.com/bagaturchess/MetatransApps_Android_APK_2DBalloons/blob/master/app/src/main/java/com/stoptheballs/app/Application_StopTheBalls_APK.java
  - https://github.com/bagaturchess/MetatransApps_Android_APK_2DBalloons/blob/master/app/src/main/java/com/stoptheballs/cfg/ads/AdsConfigurations_StopTheBalls.java
