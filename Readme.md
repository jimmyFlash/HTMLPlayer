HTML player (E-detailing) app.

app basically displays a set of local HTML pages divided into categories, accompanied by PDF and
video.

this sort of apps. is used in medical marketing CLM (Closed Loop Marketing) to market / collect surveys
on a new medical product from doctors or pharmacists.

the app provides the below functionality natively:

- HTMLs are loaded and displayed in tab bar, each tab representing a category / product from json map file
- a javascript bridge allows for two communication between the native app and the loaded html page
  to send and receive data
- in app custom video player plays video files shipped with the app accessed from HTML <a></a> href links
- app can switch between certain slides in different categories if  a nested href <a> points to a slide
  in another category or load an external web page if not found in the local html assets
- bottom recycler view displays thumbnails of the loaded category set pages & can be used for quick navigation
- app copies PDF files to external storage once the app is loaded for 1st time, files can be launched from
  links in the html pages and opened in respective pdf file reader if found on users device or display message if no apps found