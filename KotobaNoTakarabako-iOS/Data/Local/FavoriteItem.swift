import SwiftData
import Foundation

@Model
class FavoriteItem {
    var itemId: String
    var type: String   // "quote" | "word"
    var createdAt: Date

    init(itemId: String, type: String) {
        self.itemId = itemId
        self.type = type
        self.createdAt = Date()
    }
}
