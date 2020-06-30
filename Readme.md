API endpoint - https://j9l4zglte4.execute-api.us-east-1.amazonaws.com/api/ctl/weather

For weather data, send a POST request to the above end-point. Your request should have a JSON payload containing a key of zipcode and a value of the user’s zip code.
This service endpoint will return a JSON response with all necessary data to be used in your application. 


data to fetch - 
    1. Current temp
    2. current weather
    3. current day +1 weather with avg temp
    4. current day +2 weather with avg temp
    5. current day +3 weather with avg temp
    
Update the location using in `update locaiton activity` using another zipcode and display the same records in the `dashboard activity`


Sample zipcode - 
62704
94203 
33124 
21201 
29020
07039
06101 
