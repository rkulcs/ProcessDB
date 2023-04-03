export class Process {
  id = NaN
  name = ''
  filename = ''
  os = ''
  description = ''
  comments = []

  constructor(id, name, filename, os, description, comments) {
    this.id = id
    this.name = name
    this.filename = filename
    this.os = os
    this.description = description
    this.comments = comments
  }

  static emptyProcess() {
    return new Process('', '', '', '', '', '')
  }

  static objectToProcess(jsonObject) {
    return new Process(
        jsonObject.id, jsonObject.name, 
        jsonObject.filename, jsonObject.os, 
        jsonObject.description, jsonObject.comments
      )
  }

  copyJsonValues(jsonObject) {
    this.id = jsonObject.id
    this.name = jsonObject.name
    this.filename = jsonObject.filename
    this.os = jsonObject.os
    this.description = jsonObject.description
    this.comments = jsonObject.comments
  }
}

export default Process
