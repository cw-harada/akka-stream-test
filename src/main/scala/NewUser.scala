import scalikejdbc._

object NewUser extends SQLSyntaxSupport[User] {

  override val tableName = "new_user"

  val u = User.column

  def insertAll(insertUser: User)(implicit session: DBSession = AutoSession) =
    applyUpdate {
      insert.into(NewUser).namedValues(
        u.name -> insertUser.name,
        u.age -> insertUser.age
      )
    }
}