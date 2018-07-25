import org.reactivestreams.Publisher
import scalikejdbc._
import scalikejdbc.streams._
import scala.concurrent.ExecutionContext.Implicits._

case class User(id: BigInt, name: String, age: Int)

object User extends SQLSyntaxSupport[User] {

  override def columns: Seq[String] = Seq("id", "name", "age")

  override val tableName = "user"

  def apply(c: SyntaxProvider[User])(rs: WrappedResultSet): User = apply(c.resultName)(rs)

  def apply(c: ResultName[User])(rs: WrappedResultSet): User = new User(
    rs.bigInt(c.id), rs.string(c.name), rs.int(c.age))

  val u = User.syntax("u")

  def streamAll(): Publisher[User] = DB readOnlyStream {
    withSQL {
      select.from(User as u)
    }
      .map(rs => User(u)(rs)).iterator
  }

}