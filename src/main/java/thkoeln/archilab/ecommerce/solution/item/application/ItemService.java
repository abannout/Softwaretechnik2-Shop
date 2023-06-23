package thkoeln.archilab.ecommerce.solution.item.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.ShopException;
import thkoeln.archilab.ecommerce.domainprimitives.Money;
import thkoeln.archilab.ecommerce.solution.item.domain.Item;
import thkoeln.archilab.ecommerce.solution.item.domain.ItemRepository;
import thkoeln.archilab.ecommerce.usecases.ItemCatalogUseCases;
import thkoeln.archilab.ecommerce.usecases.domainprimitivetypes.MoneyType;

import java.util.UUID;

@Service
public class ItemService implements ItemCatalogUseCases {

    private ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void addItemToCatalog(UUID itemId, String name, String description, Float size, MoneyType purchasePrice, MoneyType sellPrice) {

        if (itemId == null || name == null || name.isEmpty() || description == null || description.isEmpty()
                || size != null && size < 0 || purchasePrice == null || purchasePrice.getAmount() <= 0
                || sellPrice == null
                || sellPrice.getAmount() <= 0 || sellPrice.getAmount() < purchasePrice.getAmount()) {
            throw new ShopException("input invalide!");
        } else if (itemRepository.existsById(itemId)) {
            throw new ShopException("item does  exist!");
        } else {
            Item item = new Item(itemId, name, description, size, (Money) purchasePrice, (Money) sellPrice);
            itemRepository.save(item);
        }
    }

    @Override
    public void removeItemFromCatalog(UUID itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ShopException("Item does not exist!");
        } else if (getItem(itemId).getReservedQuantity() > 0 || getItem(itemId).isExistInOrder()) {
            throw new ShopException("item exist in order or shopping cart");
        } else if (getItem(itemId).getQuantity() != 0) {
            throw new ShopException("item exist in inventory");
        }
        var item = getItem(itemId);
        itemRepository.delete(item);
    }

    @Override
    public MoneyType getSellPrice(UUID itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new ShopException("item does not exist!");
        } else {
            var optionalItem = itemRepository.findById(itemId);
            return optionalItem.map(Item::getSellPrice).orElse(null);
        }
    }

    @Override
    public void deleteItemCatalog() {
        itemRepository.deleteAll();
    }


    private Item getItem(UUID itemId) {
        var item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ShopException("item does not exist");
        }
    }


}
