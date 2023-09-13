import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comment.reducer';

export const CommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commentEntity = useAppSelector(state => state.comment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentDetailsHeading">
          <Translate contentKey="odiseyDocApp.comment.detail.title">Comment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentEntity.id}</dd>
          <dt>
            <span id="text">
              <Translate contentKey="odiseyDocApp.comment.text">Text</Translate>
            </span>
          </dt>
          <dd>{commentEntity.text}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="odiseyDocApp.comment.date">Date</Translate>
            </span>
          </dt>
          <dd>{commentEntity.date ? <TextFormat value={commentEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="author">
              <Translate contentKey="odiseyDocApp.comment.author">Author</Translate>
            </span>
          </dt>
          <dd>{commentEntity.author}</dd>
          <dt>
            <Translate contentKey="odiseyDocApp.comment.dodument">Dodument</Translate>
          </dt>
          <dd>{commentEntity.dodument ? commentEntity.dodument.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comment/${commentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentDetail;
