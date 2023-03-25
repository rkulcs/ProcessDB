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

  static emptyLogin() {
    return {
      username: '',
      password: ''
    }
  }

  static emptyRegistration() {
    return {
      username: '',
      password: '',
      passwordConfirmation: ''
    }
  }
}
