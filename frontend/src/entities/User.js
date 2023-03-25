export class User {
  id = NaN
  username = ''
  token = ''

  constructor(id, username, token) {
    this.id = id
    this.username = username
    this.token = token
  }

  static emptyUser() {
    return new User('', '', '')
  }
}
