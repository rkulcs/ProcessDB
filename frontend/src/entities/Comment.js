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

  toSimplifiedJSON() {
    return {
      "safe": this.safe,
      "info": this.info
    }
  }

  static emptyComment() {
    return new Comment('', '', true, '')
  }

  static objectToComment(jsonObject) {
    return new Comment(
      jsonObject.id, jsonObject.author, jsonObject.safe, jsonObject.info
    )
  }
}

export default Comment
