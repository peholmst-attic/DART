# Resource Messages

TO DO Describe what resources are.

## Request/response messages

### get_all_resources

This message is sent when the sender wants to know all the resources in the system.
It is typically sent only once when the sender starts up, e.g. in order to populate a UI.
This information is expected to change very rarely.

Request:
```JSON
{
  "header": {
    "type": "get_all_resources",
    "version": "1.0"
  },
  "body": {
    "disabled_included": true
  }
}
```

* `disabled_included`: specifies whether disabled ("softly deleted") resources should be included in the response.
*Optional*, default value is `false`.

Response:
```JSON
{
  "header": {
    "type": "all_resources",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z",    
  },
  "body": {
    "resources": [
      {
        "resource": "RVSPG31",
        "capabilities": ["PUMPER", "WATER_RESCUE", "SCBA"]
      },
      {
        "resource": "RVSPG21",
        "capabilities": ["PUMPER", "WATER_RESCUE", "SCBA", "TERRAIN_RESCUE"]
      },
      {
        "resource": "RVSPG33",
        "capabilities": [],
        "disabled": true
      }
    ]
  }
}
```
* `resources`: an array of the resources. If none have been entered into the system, this array is empty. *Required.*
 * `resource`: the identifier of the resource. *Required.*
 * `capabilities`: an array of the capabilities of the resource. Can be empty. *Required.*
 * `disabled`: whether the resource is disabled or not. *Optional*, default value is `false`.

### get_all_resource_capabilities

This message is sent when the sender wants to know all the capabilities a resource can have.
It is typically sent only once when the sender starts up, e.g. in order to populate a UI.
This information is expected to change very rarely.

Request:
```JSON
{
  "header": {
    "type": "get_all_resource_capabilities",
    "version": "1.0"
  }
}
```

Response:
```JSON
{
  "header": {
    "type": "all_resource_capabilities",
    "version": "1.0",
    "timestamp": "2016-05-13T21:30:59Z",    
  },
  "body": {
    "capabilities": [
      {
        "capability": "INCIDENT_COMMAND",
        "description": {
          "en": "Incident commander",
          "fi": "Pelastustoimen johtaja"
        }
      },
      {
        "capability": "WATER_RESCUE",
        "description": {
          "en": "Water rescue",
          "fi": "Pintapelastus"
        }
      }
    ]
  }
}
```
* `capabilities`: an array of the resource capabilities. If none have been entered into the system,
this array is empty. *Required.*
  * `capability`: identifier of the capability, used in other messages. *Required.*
  * `description`: human readable descriptions in different languages.
  The keys are ISO 639 language codes. *Optional.*

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
        "location_tracking": false,
        "color": "#ff0000",
      },
      {
        "state": "EN_ROUTE",
        "description": {
          "en": "En route",
          "fi": "Matkalla"
        },
        "location_tracking": true,
        "color": "#00ff00"
      }
    ]
  }
}
```

* `states`: an array of the resource states. If no states have been entered into the system,
this array is empty. *Required.*
  * `state`: identifier of the state, used in other messages. *Required.*
  * `description`: human readable descriptions in different languages.
  The keys are ISO 639 language codes. *Optional.*
  * `location_tracking`: wether the sender should report the real time location of the resource whenever this
  state is active or not. *Required.*
  * `color`: the color of the status to be used in UIs, in hexadecimal #RRGGBB format. *Optional.*

### get_current_resource_status

This message is sent when the sender wants to know the current status (state and location) of
at least one specific resource.

Request:
```JSON
{
  "header": {
    "type": "get_current_resource_status",
    "version": "1.0"
  },
  "body": {
    "resources": ["RVSPG31", "RVSPG21"]
  }
}
```

* `resources`: an array containing at least one resource identifier. *Required.*

Response:

```JSON
{
  "header": {
    "type": "current_resource_status",
    "version": "1.0",
    "timestamp": "2016-05-13T22:34:27Z"
  },
  "body": {
    "status": [
      {
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
      },
      {
        "resource": "RVSPG21",
        "state": {
          "state": "EN_ROUTE",
          "last_changed": "2016-05-12T21:25:33Z"
        }
      }
    ]
  }
}
```
* `status`: an array of the status of the requested resources. Any resources that where
not found will be missing from the array. This means the array can be empty. *Required.*
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
either the state of the resource, its geographical location or both. This is a
fire-and-forget command and it does *not* expect a response, even if the command
fails for some reason.

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
