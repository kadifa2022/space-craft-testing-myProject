import com.cydeo.spacecraft.dto.CreateHitDTO;
import com.cydeo.spacecraft.entity.Game;
import com.cydeo.spacecraft.entity.Player;
import com.cydeo.spacecraft.entity.Target;
import com.cydeo.spacecraft.enumtype.AttackType;
import com.cydeo.spacecraft.repository.GameRepository;
import com.cydeo.spacecraft.service.impl.CreateGameServiceImpl;
import com.cydeo.spacecraft.service.impl.CreateHitServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateHitServiceImplTest {


    private CreateHitServiceImpl createHitService;
     @Mock
      private GameRepository gameRepository;

    @BeforeEach
    public void setUp(){
       createHitService = new CreateHitServiceImpl(gameRepository);
    }

    @Test
    public void should_throw_exception_when_game_is_already_ended() {
        //given

        CreateHitDTO createHitDTO = new CreateHitDTO();
        createHitDTO.setGameId(1L);
        createHitDTO.setAttackType(AttackType.PLAYER_TO_TARGET);

        Game game = new Game();
        game.setId(1L);
        game.setIsEnded(true);

        //when ( set behavior) action
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));


        //then
        assertThrows(RuntimeException.class, () -> createHitService.createHit(createHitDTO));


    }
        @Test
        public void should_decrease_first_target_health_when_only_one_target_health_greater_than_zero(){
        //given value
        CreateHitDTO createHitDTO = new CreateHitDTO();
        createHitDTO.setAttackType(AttackType.PLAYER_TO_TARGET);
        createHitDTO.setGameId(1L);

        Target target = new Target();
        target.setHealth(1000);
        target.setArmor(40);
        target.setId(1L);

        Target target1 = new Target();
        target1.setHealth(0);
        target1.setArmor(40);
        target1.setId(2L);

        Set<Target> targetSet = new HashSet<>();

        targetSet.add(target1);
        targetSet.add(target);

        Player player= new Player();
        player.setHealth(200);
        player.setShootPower(250);

        Game game = new Game();
        game.setTargets(targetSet);
        game.setPlayer(player);
        game.setIsEnded(false);

        //when
         when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
         when(gameRepository.save(any())).thenReturn(game);

         //then
            Game actualGame = createHitService.createHit(createHitDTO);
            for(Target t: actualGame.getTargets()){
                if(t.getId().equals(2L)){
                    if(t.getHealth()<0){
                        Assertions.fail();
                    }
                }else{
                    if(!t.getHealth().equals(790)){
                        Assertions.fail();
                    }
                }
            }



















        }




    }





















