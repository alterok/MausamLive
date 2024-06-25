package com.alterok.mausamlive.core.domain.repository

import android.content.Context
import android.content.res.AssetManager
import com.alterok.dataresult.DataResult
import com.alterok.dataresult.wrapInSuccessDataResult
import com.alterok.mausamlive.core.domain.model.LocalityDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class FakeLocalityRepository(val appContext: Context) :
    LocalityRepository {
    private val localities: MutableList<LocalityDomainModel> = mutableListOf()

    override suspend fun getAllLocalities(): Flow<DataResult<List<LocalityDomainModel>>> = flow {
        if (localities.isEmpty())
            emit(DataResult.Loading())

        if (localities.isEmpty()) {
            withContext(Dispatchers.IO) {
                val localityStringData = parseCSV(appContext, "weather_union_locality_data.csv")
                parseCsvData(localityStringData).apply {
                    localities.addAll(this)
                }
            }.wrapInSuccessDataResult().apply {
                emit(this)
            }
        } else emit(localities.wrapInSuccessDataResult())
    }


    private fun parseCSV(context: Context, fileName: String): String {
        val assetManager: AssetManager = context.assets
        val inputStream = assetManager.open(fileName)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = mutableListOf<String>()

        var line: String? = reader.readLine()
        while (line != null) {
            lines.add(line)
            line = reader.readLine()
        }
        reader.close()
        inputStream.close()

        return lines.joinToString("\n")
    }

    private fun parseCsvData(csvData: String): List<LocalityDomainModel> {
        val localityList = mutableListOf<LocalityDomainModel>()

        // Regular expression to match CSV fields enclosed in double quotes
        val regex = Regex("\"([^\"]*)\"")

        // Split the CSV data into lines
        val lines = csvData.lines()

        for (line in lines) {
            if (line.isNotBlank()) {
                val matches = regex.findAll(line)
                val values = matches.map { it.groupValues[1] }.toList()

                if (values.size == 6) {
                    val locality = LocalityDomainModel(
                        city = values[0],
                        localityName = values[1],
                        localityId = values[2],
                        latitude = values[3],
                        longitude = values[4],
                        deviceType = values[5].firstOrNull()?.digitToIntOrNull() ?: 0
                    )
                    localityList.add(locality)
                }
            }
        }
        return localityList
    }
}