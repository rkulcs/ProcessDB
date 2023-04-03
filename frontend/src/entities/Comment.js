export class Comment {
  id = NaN
  author = ''
  safe = ''
  info = ''

  constructor(id, author, safe, info) {
    this.id = id
    this.author = author
    this.safe = safe
    this.info = info
  }

  static emptyComment() {
    return Comment('', '', '', '')
  }

  static objectToComment(jsonObject) {
    return new Comment(
      jsonObject.id, jsonObject.author, jsonObject.safe, jsonObject.info
    )
  }
}

export default Comment
