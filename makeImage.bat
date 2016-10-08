rmdir "..\forge\image1.9.2" /S /Q
copy  ".\rs_base_plugin\dist\RSBasePlugin.jar"  ".\client\installed_plugins"
copy  ".\DoPanelPlugin\dist\DoPanelPlugin.jar"  ".\client\installed_plugins"
copy  ".\MessagingPlugin\dist\MessagingPlugin.jar"  ".\client\installed_plugins"
copy  ".\OpenDolphinInitializer\dist\OpenDolphinInitializer.jar"  ".\client\dist"
xcopy ".\client\dist" "..\forge\image1.9.2\" /D /S /E /H /C /Y /R 
xcopy ".\client\installed_plugins" "..\forge\image1.9.2\installed_plugins\" /D /S /E /H /C /Y /R
xcopy "..\forge\schema" "..\forge\image1.9.2\schema\"  /D /S /E /H /C /Y /R
rmdir "..\forge\image1.9.2\installed_plugins\.svn" /S /Q