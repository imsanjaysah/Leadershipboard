# Leadershipboard
A sample application written in Kotlin to demonstrate how to load data for RecyclerView only when user stops scrolling and reaches a position
to iprove performance of the application.

App will list 30 users with rank from 1-30. There are mock data available for each page in the assets folder. 

Total pages = 15, each page size = 2.

Intially only top 2 will be loaded.

As user scrolls if he stops at 25th position it would load page (13th page) for this position and adjacent pages (12th and 14th).

Each page would loaded only once as this might actuall a call to server which is mocked by local json data.

