export class Process {
  id = NaN
  name = ''
  filename = ''
  os = ''
  description = ''

  constructor(id, name, filename, os, description) {
    this.id = id
    this.name = name
    this.filename = filename
    this.os = os
    this.description = description
  }

  static emptyProcess() {
    return new Process('', '', '', '', '')
  }

  static objectToProcess(jsonObject) {
    return new Process(
        jsonObject.id, jsonObject.name, 
        jsonObject.filename, jsonObject.os, 
        jsonObject.description
      )
  }
}