# Resource Messages

TO DO Describe what resources are.

## Request/response messages

### get_all_resource_states

This message is sent when the sender wants to know all the states that a resource
can be in. It is typically sent only once when the sender starts up, e.g.
in order to populate a UI. This information is expected to change very rarely.

Request:

```JSON
{
  "header": {
    "type": "get_all_resource_states",
    "version": "1.0"
  }
}
```

Response:

```JSON
{
  "header": {
    "type": "all_resource_states",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z",    
  },
  "body": {
    "states": [
      {
        "state": "UNAVAILABLE",
        "description": {
          "en": "Unavailable",
          "fi": "Ei hälytettävissä"
        },
        "track_location": false,
        "color": "#ff0000",
      },
      {
        "state": "EN_ROUTE",
        "description": {
          "en": "En route",
          "fi": "Matkalla"
        },
        "track_location": true,
        "color": "#00ff00"
      }
    ]
  }
}
```

* `states`: an array of the resource states. If no states have been entered into the system,
this array can be empty. *Required.*
  * `state`: identifier of the state, used in other messages. *Required.*
  * `description`: human readable descriptions in different languages.
  The keys are ISO 639 language codes. *Optional.*
  * `track_location`: wether the sender should report the real time location of the resource whenever this
  state is active or not. *Required.*
  * `color`: the color of the status to be used in UIs, in hexadecimal #RRGGBB format. *Optional.*

### get_all_resource_types

This message is sent when the sender wants to know all the resource types.
It is typically sent only once when the sender starts up, e.g. in order to populate a UI.
This information is expected to change very rarely.

Request:
```JSON
{
  "header": {
    "type": "get_all_resource_types",
    "version": "1.0"
  }
}
```

Response:
```JSON
{
  "header": {
    "type": "all_resource_types",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z",    
  },
  "body": {
    "types": [
      {
        "type": "P3",
        "description": {
          "en": "Incident commander",
          "fi": "Päivystävä palomestari"
        }
      },
      {
        "type": "1",
        "description": {
          "en": "Pumper",
          "fi": "Sammutusauto"
        }
      }
    ]
  }
}
```
* `types`: an array of the resource types. If no types have been entered into the system,
this array can be empty. *Required.*
  * `type`: identifier of the type, used in other messages. *Required.*
  * `description`: human readable descriptions in different languages.
  The keys are ISO 639 language codes. *Optional.*

### get_current_resource_status

This message is sent when the sender wants to know the current status (state and location) of
a specific resource.

Request:
```JSON
{
  "header": {
    "type": "get_current_resource_status",
    "version": "1.0"
  },
  "body": {
    "resource": "RVSPG31"
  }
}
```

* `resource`: the resource identifier. *Required.*

Response:

```JSON
{
  "header": {
    "type": "current_resource_status",
    "version": "1.0",
    "timestamp": "2016-05-13T22:34:27Z"
  },
  "body": {
    "resource": "RVSPG31",
    "state": {
      "state": "ON_SCENE",
      "last_changed": "2016-05-13T21:30:59Z"
    },
    "location": {
      "lat": 60.310546,
      "lon": 22.297169,
      "last_changed": "2016-05-13T21:30:59Z"
    }
  }
}
```

* `resource`: the resource identifier. *Required.*
* `state`: the current state of the resource. *Optional.*
  * `state`: the state identifier. *Required.*
  * `last_changed`: the ISO 8601 timestamp when the state was last changed. *Required.*
* `location`: the current location of the resource. *Optional.*
  * `lat` and `lon`: the WGS 84 coordinates. *Required.*
  * `last_changed`: the ISO 8601 timestamp when the location was last changed. *Required.*

## Command messages

### change_resource_status

This message is sent when the status of a resource is changed. The status can be
either the state of the resource, its geographical location or both. This command
does *not* expect a response.

Command:

```JSON
{
  "header": {
    "type": "change-status",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z"    
  },
  "body": {
    "resource": "RVSPG31",
    "state": "ON_SCENE",
    "location": {
      "lat": 60.310546,
      "lon": 22.297169,
    }
  }
}
```

* `resource`: The identifier of the resource whose status has changed. *Required.*
* `state`: The identifier of the new state of the resource. *Optional.*
* `location`: The new WGS 84 coordinates of the resource. *Optional.*

## Broadcast messages

To do
