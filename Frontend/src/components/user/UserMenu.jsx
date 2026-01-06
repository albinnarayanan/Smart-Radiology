import { useState } from 'react';
import { Link } from 'react-router-dom';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { logoutUser } from '../../store/actions';
import { BiUser } from "react-icons/bi";
import { IoExitOutline } from "react-icons/io5";
import toast from 'react-hot-toast';

const UserMenu = () => {
  const {user} = useSelector((state)=>state.auth);
  const image = 'https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png';

  const [anchorEl, setAnchorEL] = useState(null);
  const open = Boolean(anchorEl);

  const handleClick = (event) =>{
    setAnchorEL(event.currentTarget);
  }
  const entity = user?.entityType;
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const logoutHandler = () =>{
    dispatch(logoutUser(navigate, toast));
  }

  const handleClose = () =>{
    setAnchorEL(null);
  }


  return (
    <div>
    <div className="flex mr-5 py-4 space-x-2 overflow-hidden" onClick={handleClick}>
  <img src={image}
  alt="profile" className="inline-block size-10 rounded-full ring-0.5 ring-white outline outline-offset-1 outline-black/5" />
    </div>
    <div>
      <Menu 
        sx={{width:"400px"}}
        id = "basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        slotProps={{
          list:{
            'aria-labelledby':'basic-button',
            sx:{width:160},
          },
        }}
        >
          <Link to={`/${entity}/dashboard`}>
            <MenuItem className="flex gap-2" onClick={handleClose}>
              <BiUser className='text-xl'/>
              <span className='font-bold text-[16px] mt-1'>{user?.name}</span>
            </MenuItem> 
          </Link>
          <MenuItem className="flex gap-2" onClick={logoutHandler}>
          <div className="font-semibold w-full gap-2 flex items-center bg-button-gradient px-4 py-1 text-white rounded-sm">
            <IoExitOutline className="text-xl"/>
              <span className="font-bold text-[16px] mt-1">
                Logout
              </span>
          </div>
        </MenuItem>

      </Menu>
    </div>
    </div>
  )
}

export default UserMenu