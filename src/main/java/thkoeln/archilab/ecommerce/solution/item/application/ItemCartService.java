package thkoeln.archilab.ecommerce.solution.item.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thkoeln.archilab.ecommerce.solution.item.domain.Item;
import thkoeln.archilab.ecommerce.solution.item.domain.ItemRepository;
import thkoeln.archilab.ecommerce.solution.shoppingcart.application.ItemCartServiceInterface;
import thkoeln.archilab.ecommerce.solution.shoppingcart.domain.AbstractItem;

import java.util.Optional;
import java.util.UUID;

@Service
public class ItemCartService implements ItemCartServiceInterface {
    private ItemRepository itemRepository;
    @Autowired

    public ItemCartService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Optional<AbstractItem> findById(UUID itemId) {
        return itemRepository.findById(itemId).map(item -> item);
    }

    @Override
    public boolean existsById(UUID itemId) {
        return itemRepository.existsById(itemId);
    }

    @Override
    public void save(AbstractItem abstractItem) {
        itemRepository.save((Item) abstractItem);
    }
}
