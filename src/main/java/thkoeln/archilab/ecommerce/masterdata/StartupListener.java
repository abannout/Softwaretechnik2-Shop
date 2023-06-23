package thkoeln.archilab.ecommerce.masterdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class StartupListener implements ApplicationListener<ContextRefreshedEvent>  {
    private Logger logger = LoggerFactory.getLogger( StartupListener.class );

    private InitialMasterDataCreator initialMasterDataCreator;

    @Autowired
    public StartupListener( InitialMasterDataCreator initialMasterDataCreator ) {
        this.initialMasterDataCreator = initialMasterDataCreator;
    }

    @Override
    public void onApplicationEvent( ContextRefreshedEvent contextRefreshedEvent ) {
        logger.info( "Initializing master data..." );
        initialMasterDataCreator.registerAllClients();
    }
}
