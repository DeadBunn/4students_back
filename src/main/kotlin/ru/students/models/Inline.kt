package ru.students.models

import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.BaseTable
import org.ktorm.schema.ColumnDeclaring
import ru.vibelab.utils.DatabaseConnection

inline fun <E : Any, T : BaseTable<E>> T.getList(predicate: (T) -> ColumnDeclaring<Boolean>): List<E> {
    return DatabaseConnection.getDatabase().sequenceOf(this).filter(predicate).toList()
}