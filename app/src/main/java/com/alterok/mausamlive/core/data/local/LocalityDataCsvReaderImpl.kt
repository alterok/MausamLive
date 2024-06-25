package com.alterok.mausamlive.core.data.local

import android.content.Context
import android.content.res.AssetManager
import com.alterok.mausamlive.core.data.local.model.LocalityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class LocalityDataCsvReaderImpl @Inject constructor() : CsvReader<List<LocalityEntity>>() {
    override fun getFileName(): String {
        return "weather_union_locality_data.csv"
    }

    override suspend fun getData(context: Context): List<LocalityEntity> {
        return parseCsvData(readCsvFile(context))
    }

    private suspend fun parseCsvData(csvData: String): List<LocalityEntity> {
        return withContext(Dispatchers.IO) {
            val localityList = mutableListOf<LocalityEntity>()

            // Regular expression to match CSV fields enclosed in double quotes
            val regex = Regex("\"([^\"]*)\"")

            // Split the CSV data into lines
            val lines = csvData.lines()

            for (line in lines) {
                if (line.isNotBlank()) {
                    val matches = regex.findAll(line)
                    val values = matches.map { it.groupValues[1] }.toList()

                    if (values.size == 6) {
                        val locality = LocalityEntity(
                            city = values[0],
                            localityName = values[1],
                            localityId = values[2],
                            latitude = values[3],
                            longitude = values[4],
                            deviceType = values[5].first().digitToIntOrNull() ?: 0
                        )
                        localityList.add(locality)
                    }
                }
            }
            localityList
        }
    }
}

abstract class CsvReader<T> {
    protected suspend fun readCsvFile(context: Context): String {
        val assetManager: AssetManager = context.assets
        val inputStream = assetManager.open(getFileName())
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = mutableListOf<String>()

        withContext(Dispatchers.IO) {
            var line: String? = reader.readLine()
            while (line != null) {
                lines.add(line)
                line = reader.readLine()
            }
            reader.close()
            inputStream.close()
        }
        return lines.joinToString("\n")
    }

    abstract fun getFileName(): String

    abstract suspend fun getData(context: Context): T
}
