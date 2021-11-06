import database.*
import database.UserDynamicSqlSupport.User.age
import database.UserDynamicSqlSupport.User.id
import database.UserDynamicSqlSupport.User.name
import database.UserDynamicSqlSupport.User.profile
import org.apache.ibatis.io.Resources
import org.apache.ibatis.session.SqlSessionFactory
import org.apache.ibatis.session.SqlSessionFactoryBuilder
import org.mybatis.dynamic.sql.SqlBuilder.isEqualTo
import org.mybatis.dynamic.sql.SqlBuilder.isGreaterThan

import java.util.*

fun createSessionFactory(): SqlSessionFactory {
    val resource = "mybatis-config.xml"
    val inputStream = Resources.getResourceAsStream(resource)
    return SqlSessionFactoryBuilder().build(inputStream)
}

fun selectSample() {
    // use はブロック内での処理が終わるとリソースをクローズする
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val user = mapper.selectByPrimaryKey(1)
        println(user)
    }
}

fun whereSample() {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val userList: List<UserRecord> = mapper.select {
            // where(name, isEqualTo("mike"))
            where(age, isGreaterThan(20))
        }
        println(userList)
    }
}

fun countSample() {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.count {
            // allRows()
            where(age, isGreaterThan(20))
        }
        println(count)
    }
}

fun insertSample() {
    val randomId = Random().nextInt(100) + 100
    val randomAge = Random().nextInt(18) + 60
    val user = UserRecord(randomId, "kendrick", randomAge, "good kid")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insert(user)
        session.commit()
        println("insert $count records")
    }
}

fun multipleInsertSample() {
    val randomId = Random().nextInt(200) + 1000
    val randomAge = Random().nextInt(18) + 60
    val userList = listOf(
        UserRecord(randomId, "kendrick", randomAge, "good kid"),
        UserRecord(randomId + 1, "kanye", randomAge, "donda")
    )
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.insertMultiple(userList)
        session.commit()
        println("insert $count records")
    }
}

fun updateSample() {
    val user = UserRecord(id = 1, profile = "update")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.updateByPrimaryKeySelective(user)
        // すべてのカラムを引数を record 型のオブジェクトで更新しようとする
        // Cause: java.sql.SQLIntegrityConstraintViolationException: Column 'name' cannot be null
        // val count = mapper.updateByPrimaryKey(user)
        session.commit()
        println("update $count records")
    }
}

fun updateSample2() {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.update {
            set(profile).equalTo("update2")
            where(id, isEqualTo(1))
        }
        session.commit()
        println("update $count records")
    }
}

fun updateSample3() {
    val user = UserRecord(profile = "update3")
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        val count = mapper.update {
            updateSelectiveColumns(user)
            where(name, isEqualTo("kendrick"))
        }
        session.commit()
        println("update $count records")
    }
}

fun deleteSample() {
    createSessionFactory().openSession().use { session ->
        val mapper = session.getMapper(UserMapper::class.java)
        // val count = mapper.deleteByPrimaryKey(100)
        val count = mapper.delete {
            where(name, isEqualTo("kendrick"))
        }
        session.commit()
        println("delete $count records")
    }
}

fun main() {
    println("----------------")
    selectSample()

    println("----------------")
    whereSample()

    println("----------------")
    countSample()

    println("----------------")
    // insertSample()
    // multipleInsertSample()
    // updateSample()
    // updateSample2()
    // updateSample3()
    // deleteSample()
}