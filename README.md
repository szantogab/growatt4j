## Growatt API client

This Kotlin / Java library logs in to server.growatt.com and retrieves data on solar panels. This library is a port of Sjord's Python Growatt API: https://github.com/Sjord/growatt_api_client 

## Usage

Create a new GrowattApi instance by logging in, then retrieve a list of plants and request details of these plants.

    val api = GrowattApi.productionApiLogin("username", "password")
    val data = api2.userEnergyData()
    data.todayValueInKwh // do something awesome with the data

## API

This library uses an official API from Growatt, reverse engineered from the Growatt mobile app. Credits go to Sjord: https://github.com/Sjord
