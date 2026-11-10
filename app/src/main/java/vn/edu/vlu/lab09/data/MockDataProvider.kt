package vn.edu.vlu.lab09.data

object MockDataProvider {

    fun getSamplePhotos(): List<PhotoItem> {
        return listOf(
            PhotoItem(
                id = 1,
                title = "Majestic Misty Mountain",
                author = "Alejandro Escamilla",
                imageUrl = "https://picsum.photos/id/10/800/600"
            ),
            PhotoItem(
                id = 2,
                title = "Serene Forest Pathway",
                author = "Paul Jarvis",
                imageUrl = "https://picsum.photos/id/15/800/600"
            ),
            PhotoItem(
                id = 3,
                title = "Autumn Lake Reflection",
                author = "Dan Rubin",
                imageUrl = "https://picsum.photos/id/28/800/600"
            ),
            PhotoItem(
                id = 4,
                title = "Sunny Coastal Shoreline",
                author = "Jerry Ferguson",
                imageUrl = "https://picsum.photos/id/29/800/600"
            ),
            PhotoItem(
                id = 5,
                title = "Architectural Modernism",
                author = "Dave Gandy",
                imageUrl = "https://picsum.photos/id/42/800/600"
            ),
            PhotoItem(
                id = 6,
                title = "Golden Wheat Harvest",
                author = "Lukas Budimaier",
                imageUrl = "https://picsum.photos/id/49/800/600"
            ),
            PhotoItem(
                id = 7,
                title = "Retro Analog Camera",
                author = "Greg Shield",
                imageUrl = "https://picsum.photos/id/250/800/600"
            ),
            PhotoItem(
                id = 8,
                title = "Alpine Mountain Ridge",
                author = "Roberto Nickson",
                imageUrl = "https://picsum.photos/id/1018/800/600"
            ),
            PhotoItem(
                id = 9,
                title = "Emerald Pine Forest",
                author = "Kalen Emsley",
                imageUrl = "https://picsum.photos/id/1025/800/600"
            ),
            PhotoItem(
                id = 10,
                title = "Tranquil Waterfall Cascade",
                author = "Bailey Zindel",
                imageUrl = "https://picsum.photos/id/1036/800/600"
            ),
            PhotoItem(
                id = 11,
                title = "Snowy Mountain Peak",
                author = "NASA Unsplash",
                imageUrl = "https://picsum.photos/id/1043/800/600"
            ),
            PhotoItem(
                id = 12,
                title = "Peaceful Sunset Horizon",
                author = "Sebastien Gabriel",
                imageUrl = "https://picsum.photos/id/1050/800/600"
            )
        )
    }
}
